import * as React from "react";
import {
    Avatar, Box, Button, ButtonGroup,
    Card,
    CardContent,
    CardHeader,
    createStyles, Grid,
    IconButton,
    makeStyles, Theme,
    Typography
} from "@material-ui/core";
import {Add, AddBoxOutlined, MoreVert} from "@material-ui/icons";

const useStyles = makeStyles((theme: Theme) => createStyles({
    card: {
        maxWidth: 345
    },
    avatar: {
        backgroundColor: theme.palette.secondary.main,
    },
}));

export const ProjectsComponent = () => {
    const styles = useStyles();
    const card = <Card className={styles.card} >
        <CardHeader
            avatar={
                <Avatar className={styles.avatar}>
                    П
                </Avatar>
            }
            action={
                <IconButton color={"primary"}>
                    <MoreVert/>
                </IconButton>
            }
            title={
                <Typography color={"primary"}>Проект 1</Typography>
            }
        >
        </CardHeader>
        <CardContent >
            <Typography variant="body2" color="textSecondary" component="p">
                This impressive paella is a perfect party dish and a fun meal to cook together with your
                guests. Add 1 cup of frozen peas along with the mussels, if you like.
            </Typography>
        </CardContent>
    </Card>;

    return <main>
        <Box m={5}>
            <Box mb={5}>
                <Button color={"primary"} variant={"outlined"}>
                    Добавить проект
                </Button>
            </Box>
            <Grid container spacing={10}>
                <Grid item>
                    {card}
                </Grid>
                <Grid item>
                    {card}
                </Grid>
                <Grid item>
                    {card}
                </Grid>
                <Grid item>
                    {card}
                </Grid>
            </Grid>
        </Box>
    </main>;
};
